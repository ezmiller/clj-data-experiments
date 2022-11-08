^:kindly/hide-code?

(ns basics
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]))


;;# Tablecloth Basics

;; ### A Brief Introduction to Tablecloth Concepts

;; What is [tablecloth](https://github.com/scicloj/tablecloth)? It is
;; a data processing library, written by Tomasz
;; Sulej (Twitter: [@generateme_blog](https://twitter.com/generateme_blog)),
;; particularly valuable for efficient in-memory data exploration of
;; tabular data.
;;
;; Its basic data entities are `Datasets` and `Columns`. These
;; entities, as we will see shortly, are defined in a library that
;; powers Tablecloth called `tech.ml.dataset`.
;;

;; #### Datasets

;; Here's a basic example of creating a dataset in Tablecloth:

(tc/dataset {:a [1 2 3 4]
             :b [5 6 7 8]
             :c [9 10 11 12]})


(def simple-ds
  (tc/dataset {:a [1 2 3 4]
             :b [5 6 7 8]
             :c [9 10 11 12]}))

(class simple-ds)

;; Notice that the `Dataset` is part of a namespace called
;; `tech.v3.dataset`, and not `tablecloth`. That is because
;; tablecloth, as the name might indicate, is a kind of wrapper over a
;; library
;; called [`tech.ml.dataset`](https://github.com/techascent/tech.ml.dataset).


;; #### Columns

;; Let's also look a bit more closely at the dataset, by looking at
;; one of its columns. We'll talk about basic dataset manipulation in
;; a bit, but we can access a column using keywords if the column name
;; is a keyword.

(:a simple-ds)

;; So unsurprisingly a column is built from another type defined in
;; `tech.ml.dataset`, a `Column`.
;;
;; What's notable about these columns is that they have a specific
;; data type. Above, the readout of the column tells us indicates that
;; the column has a datatype of `int64`.

;; This is similar to the behavior we see in other data processing
;; libraries in other languages. Why are things typed? When the type
;; of the data is known, it can be handled in memory in more efficient
;; ways.
;;

;; #### Going one level deeper: Dtype-next

;; Since we are mentioning types and looking at columns, it makes
;; sense to mention one more library that is in fact what powers
;; `tech.ml.dataset`: a library called
;; [dtype-next](https://github.com/cnuernber/dtype-next).
;; 
;; This library does several things. It provides an abstraction for
;; buffers or arrays upon which Columns are based; it defines the type
;; system for the elements of those arrays; and it interfaces with
;; Java to store data in memory in the most efficient way possible.
;;
;; I hesitated to mention this library because in a sense it is
;; something that when you are using tablecloth should be hidden from
;; view, but I chose to discuss it briefly because we still need to be
;; aware of this library from time to time, when we use some of its
;; tools.
;;

;; One example of when we still use it now is if we want to check the
;; type of a column.

(tech.v3.datatype/elemwise-datatype (:a simple-ds))

;; We'll come to some other uses of this library soon, but I also just want to illustrate quickly how it is connected to the Column and to the Dataset.
;;

;; The key enttity in dtype-next is the reader buffer (or array). We
;; can see that the column is in fact built from this entity by
;; accessing a Column's internal "data" member:

(:a simple-ds)

(.data (:a simple-ds))

(class (.data (:a simple-ds)))

;; #### End brief Intro

;;  -> [Tablecloth Docs](https://scicloj.github.io/tablecloth/index.html)


