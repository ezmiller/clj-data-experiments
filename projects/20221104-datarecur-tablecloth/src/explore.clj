(ns explore
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly-default.v1.api :as kindly-default]
            [scicloj.viz.api :as viz]
            [aerial.hanami.templates :as ht]
            [clojure.string :as s]))


(kindly-default/setup!)
(clay/start!)

;; [](https://www.nyc.gov/site/finance/taxes/property-annualized-sales-update.page)


(def ds (tc/dataset "./data/nyc-annual-sales--merged.csv"
                    {:key-fn keyword}))

(map #(s/replace % #"," ""))

(defn clean [ds]
  (-> ds
      (tc/drop-missing :sale-price)
      (tc/update-columns {:sale-price
                          (partial map #(s/replace % #"," ""))})
      (tc/convert-types :sale-price :int64)
      (tc/drop-rows (comp #(> 1000 %) :sale-price))))

(tc/head ds)

(tc/info ds)


(tc/info cleaned-ds)

(-> cleaned-ds
    tc/info
    (tc/select-rows (comp #(= % :sale-price) :col-name)))

(-> cleaned-ds
    :sale-price
    (tech.v3.datatype.statistics/quartiles )
    )


(-> (clean ds)
    (tc/select-rows (comp #(< % 4000000) :sale-price))
    (tc/select-columns [:sale-price])
    (viz/data)
    (viz/x :sale-price)
    (viz/type [:histogram {:bin-count 100}])
    viz/viz)


(tc/write-csv! (clean ds) "cleaned.csv")

(tc/write! )
(def cleaned-from-file (tc/dataset "cleaned.csv"))
