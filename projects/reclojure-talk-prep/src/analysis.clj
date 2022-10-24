(ns analysis)

(require '[tablecloth.api :as table]
         '[scicloj.notespace.v4.api :as notespace]
         '[scicloj.kindly.kind :as kind])

(comment
  (notespace/restart! {:open-browser? true
                       :debug? true})

  (notespace/restart-events!)

  )

(def data (table/dataset "new-zulip-data.csv"))

(table/head data)

(+ 1 1)
