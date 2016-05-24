(ns frontendfi.routes.home
  (:require [frontendfi.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]

            [frontendfi.views :as views]
            [frontendfi.models.model :as model]))

(defn home-page []
  (layout/render views/main-page {:element-opts {:data model/model}}))

(defn examples []
  (layout/render views/examples-page))

(defn devcards []
  (layout/render views/devcards))

(defn reagent []
  (layout/render views/reagent-page))

(defroutes home-routes
  (GET "/rum" [] (home-page))
  (GET "/" [] (reagent))
  (GET "/examples" [] (examples))
  (GET "/devcards" [] (devcards))
  (GET "/error" [] (response/ok {:result (/ 10 0)}))
  (GET "/docs" [] (response/ok (-> "docs/docs.md" io/resource slurp))))

