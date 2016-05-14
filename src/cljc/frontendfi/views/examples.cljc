(ns frontendfi.views.examples
  (:require [rum.core :as rum]
            [frontendfi.utils :as utils]
            [frontendfi.views.controls :as controls]
            [frontendfi.views.timer-static :as timer-static]
            [frontendfi.views.timer-forced :as timer-forced]
            [frontendfi.views.timer-reactive :as timer-reactive]
            [frontendfi.views.binary-clock :as binary-clock]
            [frontendfi.views.board-reactive :as board-reactive]
            [frontendfi.views.board-cursored :as board-cursored]
            [frontendfi.views.bmi-calculator :as bmi-calculator]
            [frontendfi.views.local-state :as local-state]
            [frontendfi.views.self-reference :as self-reference]
            #?(:cljs [frontendfi.views.custom-props :as custom-props])
            #?(:cljs [frontendfi.views.form-validation :as form-validation])
            #?(:cljs [frontendfi.views.context :as context])))

(rum/defc examples []
          [:div.container
           [:div.example
            [:div.example-title "Timers"]
            [:div#timer-static
             (timer-static/timer-static "Static" @utils/*clock)]
            [:div#timer-forced
             (timer-forced/timer-forced)]
            [:div#timer-reactive
             (timer-reactive/timer-reactive)]]
           [:div.example
            [:div.example-title "Controls"]
            (controls/controls)]
           [:div.example
            [:div.example-title "Reactive binary clock"]
            [:div#binary-clock
             (binary-clock/binary-clock)]]
           [:div.example
            [:div.example-title "Reactive artboard"]
            [:div#board-reactive
             (board-reactive/board-reactive)]]
           [:div.example
            [:div.example-title "Cursor artboard"]
            [:div#board-cursored
             (board-cursored/board-cursored board-cursored/*board)]]
           [:div.example
            [:div.example-title "BMI Calculator"]
            [:div#bmi-calculator
             (bmi-calculator/bmi-calculator)]]
           [:div.example
            [:div.example-title "Form validation"]
            [:div#form-validation
             #?(:cljs (form-validation/form-validation))]]
           [:div.example
            [:div.example-title "Local state"]
            [:div#local-state
             (local-state/local-state "Clicks count")]]
           [:div.example
            [:div.example-title "Self-reference"]
            [:div#self-reference
             (self-reference/self-reference [:a [:b [:c :d [:e] :g]]])]]
           [:div.example
            [:div.example-title "Contexts"]
            [:div#context
             #?(:cljs (context/context))]]
           [:div.example
            [:div.example-title "Custom Methods and Data"]
            [:div#custom-props
             #?(:cljs (custom-props/custom-props))]]])

#?(:cljs
   (defn mount! [mount-el]
     (rum/mount (examples) mount-el)))