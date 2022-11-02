^:kindly/hide-code?

(ns basics
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]))


;;# Tablecloth Basics

;; We will use a tool called "Clay" to help us view this code
;; in a notebook format.  Let's start it.

(clay/start!)

;; ## What is Tablecloth?

;; It is a data processing library, written by Tomaz Sulej,
;; particularly valuable for efficient in-memory data exploration.

;; Its goal is to provide an easy-to-use and consisent data-processing
;; API for interacting with datasets.
;;
;; Here's a basic example of creating a dataset:

(tc/dataset {:a [1 2 3 4]
             :b [5 6 7 8]
             :c [9 10 11 12]})

;; If we look at this dataset's type, we can see that it is a special
;; type of thing.

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

;; Let's also look a bit more closely at the dataset, by looking at
;; one of its columns. We'll talk about basic dataset manipulation in
;; a bit, but we can access a columnn using keywords if it is a keyword.

(:a simple-ds)

(class (:a simple-ds))

;; So unsurprisingly a column is built from another type defined in
;; `tech.ml.dataset`, a `Column`.

;; What's notable about these columns is that they have a specific data
;; type. Above, the readout of the column tells us indicates that the
;; column has a datatype of `int64`.




;;
;; ---
;; Outline
;; * dataset creation
;; * dataset manipulation
;;   * selecting columns
;;   * selecting rows

