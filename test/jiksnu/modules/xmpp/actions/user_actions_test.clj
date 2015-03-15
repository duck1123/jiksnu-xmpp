(ns jiksnu.modules.xmpp.actions.user-actions-test
  (:require [ciste.config :refer [config]]
            [ciste.model :as cm]
            [ciste.sections.default :refer [show-section]]
            [clj-factory.core :refer [factory fseq]]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [hiccup.core :as h]
            [jiksnu.actions.domain-actions :as actions.domain]
            [jiksnu.actions.user-actions :as actions.user]
            [jiksnu.db :as db]
            [jiksnu.mock :as mock]
            [jiksnu.factory :as factory]
            [jiksnu.model :as model]
            [jiksnu.model.authentication-mechanism :as model.auth-mechanism]
            [jiksnu.model.domain :as model.domain]
            [jiksnu.namespace :as ns]
            [jiksnu.ops :as ops]
            [jiksnu.test-helper :as th]
            [jiksnu.util :as util]
            [lamina.core :as l]
            [midje.sweet :refer :all])
  (:import jiksnu.model.Domain
           jiksnu.model.User
           ))

(namespace-state-changes
 [(before :contents (th/setup-testing))
  (after :contents (th/stop-testing))])

(fact "#'actions.user/discover-user-jrd"
  (let [username (fseq :username)
        domain-name (fseq :domain)
        uri (format "acct:%s@%s" username domain-name)
        jrd-template (str "http://" domain-name "/api/lrdd?resource={uri}")
        jrd-uri (util/replace-template jrd-template uri)
        profile-url (format "https://%s/api/user/%s/profile" domain-name username)
        links [{:href profile-url :rel "self"}]
        mock-jrd (json/json-str {:links links})
        mock-profile {:preferredUsername username}
        domain (actions.domain/create {:_id domain-name
                                       :jrdTemplate jrd-template})
        http-uri (format "http://%s/%s" domain-name username)
        params {:_id uri}]
    (:links (actions.user/discover-user-jrd params)) => (contains {:href profile-url :rel "self"})
    (provided
      (ops/update-resource jrd-uri anything) => (l/success-result {:body mock-jrd}))))

