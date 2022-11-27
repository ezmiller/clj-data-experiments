(ns r4ds.2-rangle.code
  (:require [tablecloth.api :as tc] 
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v1.kind :as kinds]))

(comment
  (clay/start!)
  
  (clay/show-doc!
   "src/r4ds/2-wrangle/code.clj")

  )



;; ## thing 
(def iris-data-url
  "https://gist.githubusercontent.com/curran/a08a1080b88344b0c8a7/raw/0e7a9b0a5d22642a06d3d5b9bcbad9890c8ee534/iris.csv"
  )

(def iris-ds (tc/dataset iris-data-url {:key-fn keyword}))

(tc/head iris-ds)

(-> {:a [1 2 3]
     :b [4 5 6]}
    tc/dataset
    ;; tc/columns
    #_(->> (apply tech.v3.datatype/emap (fn [& rows]
                                  (reduce + rows))
                                :int64))
    (tc/map-columns :sum [:a :b] (fn [& rows]
                                   (println {:rows rows})
                                   (reduce + rows)))
    )

(apply tech.v3.datatype/emap (fn [& things]
                               (println {:things things})
                               (reduce + things))
       :int64
       [1 2 3]
       [4 5 6]
       [7 8 9])

(tech.v3.dataset/column-map  )

