(ns frontendfi.devcards.main
  (:require
    [frontendfi.views.context :as context])
  (:require-macros
    [devcards.core :as dc :refer [defcard deftest defcard-doc]]))

(defcard-doc "Documentation reloads, but the card itself doesn't")

(defcard context (context/context))

