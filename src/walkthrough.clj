(ns walkthrough
  (:require [tech.ml.dataset :as ds]
            [tech.v2.datatype :as dtype]
            [tech.v2.datatype.functional :as dfn]
            [tech.ml.dataset.column :as ds-col]))


;; Testing this walkthrough:
;; https://github.com/techascent/tech.ml.dataset/blob/master/docs/walkthrough.md

;; Creation
(ds/->dataset [{:a 1 :b 2}])

(ds/name-values-seq->dataset {:age [1 2 3 4 5]
                              :name ["a" "b" "c" "d" "e"]})


;; Manipulation
(def new-ds (ds/->dataset [{:a 1 :b 2} {:a 2 :c 3}]))
(first new-ds)
(last new-ds)
(new-ds :c)
(ds-col/missing (new-ds :b))
(ds-col/missing (new-ds :c))


;; Accssing col values
(ds/name-values-seq->dataset {:age [1 2 3 4 5]
                              :name ["a" "b" "c" "d" "e"]})

(def nameage *1)

;; read an entire col
(dtype/->reader (nameage :age))
(dtype/->reader (nameage :name))
(dtype/->array-copy (nameage :age))
(type *1)

;; read cols one at a time
(def col-reader (dtype/->reader (nameage :age)))
(col-reader 0)

;; read a row
(ds/value-reader nameage)

;; ames - sub-rect selection
(def ames-ds (ds/->dataset "file://data/train.csv"))

(ames-ds "KitchenQual")
(ames-ds "SalePrice")

;; "subrect" selection
(def small-ames (ds/select ames-ds ["KitchenQual" "SalePrice"] (range 5)))

;; add, update, remove columns
(ds/update-column small-ames "SalePrice" #(-> (dtype/->reader % :float64)
                                              dfn/log))
(ds/add-or-update-column small-ames "Range" (float-array (range 5)))
(ds/remove-column small-ames "KitchenQual")

;; filter
(-> (ds/ds-filter #(< 30000 (get % "SalePrice")) ames-ds)
    (ds/select ["SalePrice" "KitchenQual"] (range 5)))

;; sort-by
(-> (ds/ds-sort-by #(get % "SalePrice") ames-ds)
    (ds/select ["SalePrice" "KitchenQual"] (range 5)))
;; - what if you want to sort descending? 

(def group-map (->> (ds/select ames-ds ["SalePrice" "KitchenQual"] (range 20))
                    (ds/ds-group-by #(get % "KitchenQual"))))
;; output here is a bit awkward
