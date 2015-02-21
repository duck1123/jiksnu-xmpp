(ns jiksnu.modules.xmpp.actions.user-actions)

(defn split-jid
  [
   ;; ^JID
   jid]
  [(tigase/get-id jid) (tigase/get-domain jid)])

;; TODO: This is a special case of the discover action for users that
;; support xmpp discovery
(defn request-vcard!
  "Send a vcard request to the xmpp endpoint of the user"
  [user]
  (let [body (element/make-element
                "query" {"xmlns" ns/vcard-query})]
      (-> {:from (tigase/make-jid "" (config :domain))
           :to (tigase/make-jid user)
           :id "JIKSNU1"
           :type :get
           :body body}
          tigase/make-packet
          tigase/deliver-packet!)))

(defn fetch-updates-xmpp
  [user]
  ;; TODO: send user timeline request
  (let [packet (tigase/make-packet
                  {:to (tigase/make-jid user)
                   :from (tigase/make-jid "" (config :domain))
                   :type :get
                   :body (element/make-element
                          ["pubsub" {"xmlns" ns/pubsub}
                           ["items" {"node" ns/microblog}]])})]
      (tigase/deliver-packet! packet)))

;; TODO: xmpp case of update
(defaction fetch-remote
  [user]
  (let [domain (get-domain user)]
    (if (:xmpp domain)
      (request-vcard! user))))

;; TODO: This is the job of the filter
(defn find-or-create-by-jid
  [
   ;; ^JID
   jid]
  ;; {:pre [(instance? JID jid)]}
  (let [[username domain] (split-jid jid)]
    (find-or-create {:username username
                     :domain domain})))

(defaction xmpp-service-unavailable
  "Error callback when user doesn't support xmpp"
  [user]
  (let [domain-name (:domain user)
        domain (model.domain/fetch-by-id domain-name)]
    (actions.domain/set-xmpp domain false)
    user))

(defn process-jrd
  [user jrd & [options]]
  (log/info "processing jrd")
  (let [links (concat (:links user) (:links jrd))]
    (assoc user :links links)))

(defn fetch-jrd
  [params & [options]]
  (log/info "fetching jrd")
  (if-let [domain (get-domain params)]
    (if-let [url (model.domain/get-jrd-url domain (:_id params))]
      (if-let [response @(ops/update-resource url options)]
        (when-let [body (:body response)]
          (json/read-str body :key-fn keyword))
        (log/warn "Could not get response"))
      (log/warn "could not determine jrd url"))
    (throw+ "Could not determine domain name")))

(defn discover-user-jrd
  [params & [options]]
  (log/info "Discovering user via jrd")
  (if-let [jrd (fetch-jrd params options)]
    (process-jrd params jrd options)
    (do (log/warn "Could not fetch jrd")
        params)))


