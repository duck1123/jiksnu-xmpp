(ns jiksnu.modules.xmpp.filters.domain-filters
  (:require [ciste.filters :refer [deffilter]]
            [clojure.tools.logging :as log]
            [jiksnu.actions.domain-actions :as actions.domain]
            [jiksnu.model.domain :as model.domain])
  (:import tigase.xml.Element))

;; ping-error

(deffilter #'actions.domain/ping-error :xmpp
  [action request]
  (-> request :from .getDomain
      model.domain/fetch-by-id action))

;; ping-response

(deffilter #'actions.domain/ping-response :xmpp
  [action request]
  (-> request :from .getDomain
      model.domain/fetch-by-id action))

