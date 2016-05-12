(ns frontendfi.views.controls
  (:require
    [rum.core :as rum])
  #?(:cljs
     (:require-macros
     [devcards.core :as dc :refer [defcard deftest]])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Control panel

(def *color (atom "#FA8D97"))
(def *speed (atom 167))


;; generic “atom editor” component
(rum/defc input < rum/reactive [ref]
          [:input {:type "text"
                   :value (rum/react ref)
                   :style {:width 100}
                   :on-change #(reset! ref (.. % -target -value))}])


;; Raw top-level component, everything interesting is happening inside
(rum/defc controls []
          [:dl
           [:dt "Color: "]
           [:dd (input *color)]
           ;; Binding another component to the same atom will keep 2 input boxes in sync
           [:dt "Clone: "]
           [:dd (input *color)]
           [:dt "Tick: "]
           [:dd (input *speed) " ms"]])

