(ns frontendfi.views.timer-forced
  (:require
    [rum.core :as rum]
    [frontendfi.utils :as utils])
  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Raw component (pure react-style)


(rum/defc timer-forced []
  [:div "Forced: "
    [:span {:style {:color @utils/*color}} (utils/format-time @utils/*clock)]])


#?(:cljs
(defn mount! [mount-el]
  (let [comp (rum/mount (timer-forced) mount-el)]
    ;; Setting up watch manually,
    ;; force specific component re-render via request-render
    (add-watch utils/*clock :timer-forced
               (fn [_ _ _ _]
                 (rum/request-render comp))))))

