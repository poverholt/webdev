(defproject webdev "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [postgresql/postgresql "9.1-901-1.jdbc4"]
                 [compojure "1.6.2"]
                 [hiccup "1.0.5"]
                 [ring "1.8.2"]]

  :min-lein-version "2.0.0"

  :uberjar-name "webdev.jar"
  
  :main webdev.core
  
  :repl-options {:init-ns webdev.core}

  :profiles {:dev
             {:main webdev.core/-dev-main}})
