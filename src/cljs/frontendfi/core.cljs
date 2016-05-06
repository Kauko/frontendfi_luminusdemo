(ns frontendfi.core
  (:require [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [frontendfi.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

#_(def pages
  {:home #'home-page
   :about #'about-page})

#_(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
#_(secretary/set-config! :prefix "#")

#_(secretary/defroute "/" []
  (session/put! :page :home))

#_(secretary/defroute "/about" []
  (session/put! :page :about))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app

(defn mount-components []
  #_(r/render [#'navbar] (.getElementById js/document "navbar"))
  #_(r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  #_(hook-browser-navigation!)
  (mount-components))
