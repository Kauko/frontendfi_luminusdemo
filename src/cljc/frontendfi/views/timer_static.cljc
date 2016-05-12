(ns frontendfi.views.timer-static
  (:require
    [rum.core :as rum]
    [frontendfi.utils :as utils]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Static component (quiescent-style)


(rum/defc timer-static < rum/static [label ts]
  [:div label ": "
    [:span {:style {:color @utils/*color}} (utils/format-time ts)]])


#?(:cljs
(defn mount! [mount-el]
  (rum/mount (timer-static "Static" @utils/*clock) mount-el)
  ;; Setting up watch manually,
  ;; force top-down re-render via mount
  (add-watch utils/*clock :timer-static
             (fn [_ _ _ new-val]
               (rum/mount (timer-static "Static" new-val) mount-el)))))


