(defproject frontendfi "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[luminus-log4j "0.1.3"]
                 [metosin/compojure-api "1.1.0"]
                 [cljs-ajax "0.5.4"]
                 [secretary "1.2.3"]
                 [org.clojure/clojurescript "1.8.51" :scope "provided"]
                 [org.clojure/clojure "1.8.0"]
                 [markdown-clj "0.9.89"]
                 [ring-middleware-format "0.7.0"]
                 [metosin/ring-http-response "0.6.5"]
                 [bouncer "1.0.0"]
                 [org.webjars/bootstrap "4.0.0-alpha.2"]
                 [org.webjars/font-awesome "4.6.1"]
                 [org.webjars.bower/tether "1.3.1"]
                 [org.clojure/tools.logging "0.3.1"]
                 [compojure "1.5.0"]
                 [ring-webjars "0.1.1"]
                 [ring/ring-defaults "0.2.0"]
                 [mount "0.1.10"]
                 [cprop "0.1.7"]
                 [org.clojure/tools.cli "0.3.4"]
                 [luminus-nrepl "0.1.4"]
                 [buddy "0.13.0"]
                 [luminus-migrations "0.1.2"]
                 [conman "0.5.1"]
                 [org.postgresql/postgresql "9.4-1206-jdbc4"]
                 [org.webjars/webjars-locator-jboss-vfs "0.1.0"]
                 [luminus-immutant "0.2.0"]

                 [rum "0.8.3"]]

  :min-lein-version "2.0.0"

  :jvm-opts ["-server" "-Dconf=.lein-env"]
  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

  :main frontendfi.core
  :migratus {:store :database :db ~(get (System/getenv) "DATABASE_URL")}

  :plugins [[lein-cprop "1.0.1"]
            [migratus-lein "0.2.8"]
            [lein-cljsbuild "1.1.3"]
            [lein-sassc "0.10.4"]
            [lein-auto "0.1.2"]]
   :sassc
   [{:src "resources/scss/screen.scss"
     :output-to "resources/public/css/screen.css"
     :style "nested"
     :import-path "resources/scss"}] 
  
   :auto
   {"sassc" {:file-pattern #"\.(scss|sass)$" :paths ["resources/scss"]}} 
  
  :hooks [leiningen.sassc]
  :clean-targets ^{:protect false}
  [:target-path [:cljsbuild :builds :app :compiler :output-dir] [:cljsbuild :builds :app :compiler :output-to]]
  :cljsbuild
  {:builds
   {:app
    {:source-paths ["src/cljc" "src/cljs"]
     :compiler
     {:output-to "target/cljsbuild/public/js/app.js"
      :output-dir "target/cljsbuild/public/js/out"
      :externs ["react/externs/react.js"]
      :pretty-print true}}}}
  
  :target-path "target/%s/"
  :profiles
  {:uberjar {:omit-source true

              :prep-tasks ["compile" ["cljsbuild" "once"]]
              :cljsbuild
              {:builds
               {:app
                {:source-paths ["env/prod/cljs"]
                 :compiler
                 {:optimizations :advanced
                  :pretty-print false
                  :closure-warnings
                  {:externs-validation :off :non-standard-jsdoc :off}}}}}

             :aot :all
             :uberjar-name "frontendfi.jar"
             :source-paths ["env/prod/clj"]
             :resource-paths ["env/prod/resources"]}
   :dev           [:project/dev :profiles/dev]
   :test          [:project/test :profiles/test]
   :project/dev  {:dependencies [[prone "1.1.1"]
                                 [ring/ring-mock "0.3.0"]
                                 [ring/ring-devel "1.4.0"]
                                 [pjstadig/humane-test-output "0.8.0"]
                                 [lein-figwheel "0.5.2"]
                                 [lein-doo "0.1.6"]
                                 [com.cemerick/piggieback "0.2.2-SNAPSHOT"]

                                 [devcards "0.2.1-6"]
                                 [org.clojure/tools.namespace "0.2.11"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.14.0"]
                                 [lein-figwheel "0.5.2"]
                                 [lein-doo "0.1.6"]
                                 [org.clojure/clojurescript "1.8.51"]]

                   :cljsbuild
                   {:builds
                    {:app
                     {:source-paths ["env/dev/cljs"]
                      :compiler
                      {:main "frontendfi.app"
                       :asset-path "/js/out"
                       :optimizations :none
                       :source-map true}}
                     :devcards
                     {:source-paths ["devcards/cljs" "src/cljc" "src/cljs"]
                      :figwheel { :devcards true }
                      :compiler {:main "frontendfi.devcards.main"
                                 :asset-path "js/devcards_out"
                                 :output-to  "target/cljsbuild/public/js/frontendfi_devcards.js"
                                 :output-dir "target/cljsbuild/public/js/devcards_out"
                                 :source-map-timestamp true
                                 :optimizations :none}}
                     :test
                     {:source-paths ["src/cljc" "src/cljs" "test/cljs"]
                      :compiler
                      {:output-to "target/test.js"
                       :main "frontendfi.doo-runner"
                       :optimizations :whitespace
                       :pretty-print true}}}}

                  :figwheel
                  {:http-server-root "public"
                   :nrepl-port 7002
                   :css-dirs ["resources/public/css"]}
                  :doo {:build "test"}
                  :source-paths ["env/dev/clj" "test/clj"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options {:init-ns user
                                 :init (frontendfi.core/-main)
                                 :nrepl-middleware
                                 [cemerick.piggieback/wrap-cljs-repl]}
                  :injections [(require 'pjstadig.humane-test-output)
                               (pjstadig.humane-test-output/activate!)]}
   :project/test {:resource-paths ["env/dev/resources" "env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
