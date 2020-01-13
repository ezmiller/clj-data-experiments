(ns benchmarks
  (:use [criterium.core])
  (:require [tech.ml.dataset :as ds]
            [tech.v2.datatype :as dtype]
            [tech.v2.datatype.functional :as dfn]
            [tech.v2.datatype.protocols :as dtype-proto]
            [tech.ml.dataset.column :as ds-col]
            [kixi.stats.core :as kixi]
            [kixi.stats.protocols :as kixi-proto]
            [fastmath.stats :as fm-stats]
            [clojure.math.numeric-tower :as m]
            [clojure.repl :refer [doc source]]))

(def test-ds
  (ds/name-values-seq->dataset
   {:nums (take (m/expt 10 7) (repeatedly #(rand-int 100)))}))
(def nums-reader (dtype/->reader (test-ds :nums) :int32))

;; Here's a fn where datatype and fastmath are using the same
;; apache commons library, though not the same api. Here the api
;; used by tech.datatype is faster actually! 
(with-progress-reporting (quick-bench (dfn/variance nums-reader)))
;; => Execution time mean : 602.789530 ms
(with-progress-reporting (quick-bench (fm-stats/variance nums-reader)))
;; => Execution time mean : 2.190040 sec

(with-progress-reporting (quick-bench (dfn/kurtosis nums-reader)))
;; => Execution time mean : 7.951409 sec
(with-progress-reporting (quick-bench (fm-stats/kurtosis nums-reader)))
;; => Execution time mean : 4.016901 sec

(with-progress-reporting (quick-bench (dfn/standard-error nums-reader)))
;; => Execution time mean : 1.662718 sec
(with-progress-reporting (quick-bench (fm-stats/sem nums-reader)))
;; => Execution time mean : 1.929747 sec

(with-progress-reporting (quick-bench (dfn/standard-deviation-population nums-reader)))
;; => Execution time mean : 1.864944 sec
(with-progress-reporting (quick-bench (fm-stats/population-stddev nums-reader)))
;; => Execution time mean : 1.898085 sec



