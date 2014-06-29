(ns jiksnu.model.subscription
  (:require [clj-tigase.core :as tigase]
            [clj-tigase.element :as element]
            [clj-time.core :as time]
            [clojure.tools.logging :as log]
            [jiksnu.model :as model]
            [jiksnu.model.user :as model.user]
            [jiksnu.namespace :as ns]
            [jiksnu.templates.model :as templates.model]
            [slingshot.slingshot :refer [throw+]]
            [validateur.validation :refer [validation-set presence-of]])
  (:import jiksnu.model.Subscription
           org.bson.types.ObjectId
           org.joda.time.DateTime))

(defn subscriptions-request
  "returns a xmpp packet requesting subscriptions"
  [from to]
  (tigase/make-packet
   {:to (tigase/make-jid to)
    :from (tigase/make-jid from)
    :type :get
    :body (element/make-element
           ["pubsub" {"xmlns" ns/pubsub}
            ["subscriptions" {"node" ns/microblog}]])}))

(defn subscribers-request
  [from to]
  (tigase/make-packet
   {:to (tigase/make-jid to)
    :from (tigase/make-jid from)
    :type :get
    ;; :id (fseq :id)
    :body (element/make-element
           "pubsub" {"xmlns" ns/pubsub}
           ["subscribers" {"node" ns/microblog}])}))
