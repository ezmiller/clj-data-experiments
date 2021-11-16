(ns scratch 
  (:require [scicloj.notespace.v4.api :as notespace] ; the Notespace API
            [scicloj.kindly.api :as kindly] ; specifying kinds of notes
            [scicloj.kindly.kind :as kind] ; a collection of known kinds of notes
            [scicloj.kindly.kindness :as kindness]
            [tech.v3.dataset :as tmd]
            [tablecloth.api :as tbl]
            [tech.v3.datatype.datetime :as dtdt]
            [tech.v3.datatype.functional :as fun]
            [tablecloth.time.api :as time]))

(first (:timestamp data))
;; => 1542211640

(time/convert-to 1542211640 :instant)
(dtdt/milliseconds->datetime :instant 1542211640 )

tbl/add-column
(tbl/drop )
(comment
  ;; (clerk/serve! {:browse? true})
  ;; (clerk/show! "src/scratch.clj")
  ;; (clerk/serve! {:watch-paths ["src"]})

  (notespace/restart! #_{:open-browser? true})
  
  (notespace/restart-events!)

  )

;; (require '[nextjournal.clerk :as clerk])

(def raw-data (read-string (slurp "zulip-scicloj.txt")))

(-> raw-data first keys)

(take 1 raw-data)

(->> (take 1 raw-data)
     (map (juxt :id :sender_id :type :timestamp)))

(def data (->> raw-data
               (map #(select-keys % [:id :stream_id :subject :sender_id :type :timestamp :content]))
               (#(tbl/dataset % #_{:parser-fn {:timestamp [:local-date-time
                                                        (fn [el] (dtdt/milliseconds->datetime :local-date-time (* 1000 el)))]}}))))

^kind/dataset
(tbl/head data)

^kind/dataset
(tbl/info data)

^kind/dataset
(-> data
    (tbl/select-rows (comp #(= "local data science courses" %) :subject)))


(def data-with-diff
  (let [shifted (fun/shift (:timestamp data) 1)]
    (-> data
        (tbl/add-column :time-since-previous
                        #(-> %
                             :timestamp
                             (->> (fun/- shifted))
                             (fun/abs))))))

(tbl/head data-with-diff)


