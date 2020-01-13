(ns titanic
  (:require [tech.ml.dataset :as ds]
            [tech.v2.datatype :as dtype]
            [tech.v2.datatype.functional :as dfn]
            [tech.v2.datatype.protocols :as dtype-proto]
            [tech.ml.dataset.column :as ds-col]
            [kixi.stats.core :as kixi]
            [kixi.stats.protocols :as kixi-proto]
            [oz.core :as oz]
            [clojure.pprint :as pp]
            [clojure.repl :refer [doc source]]))

;; Based on:
;; https://www.kaggle.com/mrisdal/exploring-survival-on-the-titanic

;; Start server
;; (oz/start-server!)

(def base-data-path "file://data/titanic/")

(def ttrain (ds/->dataset (apply str base-data-path "train.csv")))
(def ttest (ds/->dataset (apply str base-data-path "test.csv")))
(def full (ds/ds-concat
           ttest
           (ds/select ttrain (remove #{"Survived"} (ds/column-names ttrain)) :all)))

;; Get oriented

;; Why is shape a fn in datatype and not in dataset?
(dtype/shape full)

;; Feels like I shouldn't need to use select here just to get
;; the cols I want.
(ds/select
 (ds/descriptive-stats full)
 [:col-name :datatype :n-valid :n-missing]
 :all)

;; why do we need a "reader"? why not just access the column?
;; a "reader" adds a layer of abstraction, that is only somewhat clear.
;; (doc dtype/->reader)
;; (doc dtype/->>reader)
(def titles
  (->> (full "Name")
    (dtype/->reader)
    (map #(clojure.string/replace % #"(.*, )|(\..*)" ""))))

(def full+ (ds/add-or-update-column full "Title" titles))

(ds/select
 (ds/descriptive-stats full+)
 [:col-name :datatype :n-valid :n-missing]
 :all)

(defn sex-v-title-xtab [dataset]
  (-> (transduce
        identity
        (kixi/cross-tabulate #(get % "Sex") #(get % "Title"))
        (ds/->flyweight (ds/select dataset ["Sex" "Title"] :all)))))

;; Should there be a more standard way to print the xtab?
(defn print-margin-totals [xtab]
  (doseq [ts (kixi-proto/margin-totals xtab)]
    (pp/print-table [ts])))

;; flyweight blows up if there are missing values in some columns.
;; (ds/->flyweight full+)

;; (ds/->flyweight (ds/select full+ ["Sex" "Title"] :all))
;; returns row-by-row  objects


;; Clean up titles
(defn normalize-to-ms [x]
  (map (fn [title]
         (if (some #(= title %) ["Mlle" "Ms" "Mme"]) "Ms" title)) x))

(defn convert-rare [x]
  (map (fn [title]
         (let [rare ["the Countess" "Col" "Don" "Dr"
                     "Major" "Rev" "Jonkheer" "Lady"
                     "Master" "Capt" "Dona" "Sir"]]
           (if (some #(= % title) rare) "Rare Title" title))) x)))

(defn clean-title-col [dataset]
  (->> (dataset "Title")
      normalize-to-ms
      convert-rare
      (ds/add-or-update-column full+ "Title")))


(def cleaned (-> full+
                 clean-title-col))

(-> cleaned
    sex-v-title-xtab
    print-margin-totals)

