(ns jiksnu.model.domain-test
  (:require [clj-factory.core :refer [factory]]
            [clojure.tools.logging :as log]
            [jiksnu.actions.domain-actions :as actions.domain]
            [jiksnu.mock :as mock]
            [jiksnu.model :as model]
            [jiksnu.model.domain :refer [create drop! get-xrd-url ping-request]]
            [jiksnu.test-helper :refer [context test-environment-fixture]]
            [midje.sweet :refer [=> contains]])
  (:import jiksnu.model.Domain))

(test-environment-fixture

 (context #'ping-request
   (drop!)
   (let [domain (mock/a-domain-exists)]
     (ping-request domain) => (contains {:body e/element?})))

 )
