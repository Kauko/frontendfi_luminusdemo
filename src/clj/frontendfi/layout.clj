(ns frontendfi.layout
  (:require [markdown.core :refer [md-to-html-string]]
            [ring.util.http-response :refer [content-type ok]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [hiccup.core :as hiccup]
            [frontendfi.views :refer [error]]))

(declare ^:dynamic *identity*)
(declare ^:dynamic *app-context*)

(defn render
  [hiccup & [params]]
  (content-type
    (ok
      (hiccup/html (hiccup (assoc params
                             :csrf-token *anti-forgery-token*
                             :servlet-context *app-context*))))
    "text/html; charset=utf-8"))

(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)
   :message - detailed error message (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (error (merge
                       error-details
                       {:message "Luckily it's not the end of the world!"}))})
