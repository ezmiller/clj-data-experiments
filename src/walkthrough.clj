(ns walkthrough
  (:require [tech.ml.dataset :as ds]
            [tech.v2.datatype :as dtype]
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
