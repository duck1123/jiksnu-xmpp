(ns jiksnu.actions.domain-actions
  (:require [ciste.config :only [config]]
            [ciste.core :only [defaction]]
            [ciste.initializer :only [definitializer]]
            [ciste.model :as cm]
            [clj-tigase.core :as tigase]
            [clj-time.core :as time]
            [clojure.core.incubator :refer [-?>>]]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [jiksnu.model :as model]
            [jiksnu.model.domain :as model.domain]
            [jiksnu.model.webfinger :as model.webfinger]
            [jiksnu.ops :as ops]
            [jiksnu.templates.actions :as templates.actions]
            [jiksnu.transforms :as transforms]
            [jiksnu.transforms.domain-transforms :as transforms.domain]
            [jiksnu.util :as util]
            [lamina.core :as l]
            [lamina.time :as lt]
            [lamina.trace :as trace]
            [slingshot.slingshot :only [throw+ try+]])
  (:import java.net.URL
           jiksnu.model.Domain))

(defn discover-onesocialweb
  [domain url]
  (-> domain
      model.domain/ping-request
      tigase/make-packet
      tigase/deliver-packet!)
  domain)

