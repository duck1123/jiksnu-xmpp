(ns jiksnu.modules.xmpp.views.domain-views
  (:require [ciste.model :as cm]
            [ciste.views :only [defview]]
            [clojure.tools.logging :as log]
            [jiksnu.actions.domain-actions :as actions.domain]
            [jiksnu.model.domain :as model.domain])
  (:import jiksnu.model.Domain
           jiksnu.model.User))

(defview #'actions.domain/ping :xmpp
  [request domain]
  (model.domain/ping-request domain))

(defview #'actions.domain/ping-error :xmpp
  [request _]
  (cm/implement))

(defview #'actions.domain/ping-response :xmpp
  [request _domain]
  (cm/implement)
  #_{:status 303
     :template false
     :headers {"Location" (named-path "index domains")}})

