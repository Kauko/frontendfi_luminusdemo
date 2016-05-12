(ns frontendfi.views.timer-reactive
  (:require
    [rum.core :as rum]
    [frontendfi.utils :as utils])
  )


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Reactive components (reagent-style)


;; regular static top-down component with immutable args
(rum/defc colored-clock < rum/static [time color]
  [:span {:style {:color color}} (utils/format-time time)])


(rum/defc timer-reactive < rum/reactive []
  [:div "Reactive: "
    ;; Subscribing to atom changes with rum/react
    ;; Then pass _dereferenced values_ to static component
    (colored-clock (rum/react utils/*clock) (rum/react utils/*color))])


;; After initial mount, all changes will be re-rendered automatically
#?(:cljs
(defn mount! [mount-el]
  (rum/mount (timer-reactive) mount-el)))

