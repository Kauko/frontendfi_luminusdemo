(ns frontendfi.views.context
  (:require
    [rum.core :as rum]
    [frontendfi.utils :as utils])
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest defcard-doc]]))

;; Components with context that all descendants have access to implicitly.

;; This is useful when you are using child components you cannot modify. 
;; For example, a JS library that gives you components which rely on a context
;; value being set by an ancestor component.


(rum/defcc rum-context-comp < { :class-properties { :contextTypes {:color js/React.PropTypes.string}}}
  [comp]
  [:span
    { :style { :color (.. comp -context -color) }}
    "CHILD component uses context to set font color."])


;; Assume the following component is from our source code.
(def color-theme
  { :child-context (fn [state] {:color @utils/*color})
    :class-properties { :childContextTypes {:color js/React.PropTypes.string} } })


(rum/defc context < color-theme []
  [:div
    [:div "Root component implicitly passes data to descendants."]
    (rum-context-comp)])


(defn mount! [mount-el]
  (rum/mount (context) mount-el))

(defcard-doc "This will only change after (reset-autobuild)")