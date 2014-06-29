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

