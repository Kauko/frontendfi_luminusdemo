(ns frontendfi.views
  (:require [hiccup.page :as page]
            [hiccup.element :as el]
            [rum.core :as rum]
            [frontendfi.views.app :refer [app]]
            [devcards.core :as devcards]))

(defn- boilerplate-js [token context]
  (str "var context = " context ";
  var csrfToken = " token ";"))

(def error-css
  " html {
        height: 100%;
                min-height: 100%;
        min-width: 100%;
        overflow: hidden;
        width: 100%;
        }
  html body {
             height: 100%;
                     margin: 0;
                     padding: 0;
                     width: 100%;
             }
  html .container-fluid {
                         display: table;
                         height: 100%;
                                  padding: 0;
                                  width: 100%;
                         }
  html .row-fluid {
                   display: table-cell;
                   height: 100%;
                            vertical-align: middle;
                   }")

(rum/defc main [{:keys [csrf-token servlet-context]}]
          [:html
           [:head
            [:meta
             {:http-equiv "Content-Type", :content "text/html; charset=UTF-8"}]
            [:meta
             {:name "viewport", :content "width=device-width, initial-scale=1"}]
            [:title "Welcome to frontendfi"]]
           [:body
            [:div#navbar]
            [:div#app
             (app)]
            [:link
             {:type "text/css",
              :rel  "stylesheet",
              :href "/assets/bootstrap/css/bootstrap.min.css"}]
            [:link
             {:type "text/css",
              :rel  "stylesheet",
              :href "/assets/font-awesome/css/font-awesome.min.css"}]
            [:link
             {:type "text/css", :rel "stylesheet", :href "/css/screen.css"}]
            [:script
             {:type "text/javascript"}
             (boilerplate-js csrf-token servlet-context)]
            [:script {:type "text/javascript", :src "/js/app.js"}]]])

(defn error [{:keys [status title message]}]
  (page/html5
    {}
    [:head
     [:title "Something bad happened"]
     [:meta
      {:content "text/html; charset=UTF-8", :http-equiv "Content-Type"}]
     [:meta
      {:content "width=device-width, initial-scale=1.0",
       :name    "viewport"}]
     (page/include-css "/assets/bootstrap/css/bootstrap.min.css")
     (page/include-css "/assets/font-awesome/css/font-awesome.min.css")
     [:style
      {:type "text/css"}
      error-css]]
    [:body
     [:div.container-fluid
      [:div.row-fluid
       [:div.col-lg-12
        [:div.centering.text-center
         [:div.text-center
          [:h1 [:span.text-danger (str "Error: " status)]]
          [:hr]
          (when title [:h2.without-margin title])
          (when message [:h4.text-danger message])]]]]]]))

(rum/defc devcards [params]
  [:html
   [:head
    [:meta
     {:content
            "width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no",
      :name "viewport"}]
    [:meta {:charset "UTF-8"}]]
   [:body
    [:link
     {:type "text/css",
      :rel  "stylesheet",
      :href "/assets/bootstrap/css/bootstrap.min.css"}]
    [:link
     {:type "text/css",
      :rel  "stylesheet",
      :href "/assets/font-awesome/css/font-awesome.min.css"}]
    [:link
     {:type "text/css", :rel "stylesheet", :href "/css/screen.css"}]
    [:script {:type "text/javascript", :src "/js/frontendfi_devcards.js"}]]])