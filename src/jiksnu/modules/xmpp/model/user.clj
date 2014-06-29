;; deprecated
;; TODO: Split the jid into it's parts and fetch.
(defn fetch-by-jid
  [jid]
  (get-user (.getLocalpart jid)
            (.getDomain jid)))

