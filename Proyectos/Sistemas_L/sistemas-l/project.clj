(defproject sistemas-l "0.1.0-SNAPSHOT"
  :description "Sistema-L en Clojure"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]]
  :main sistemas-l.core
  :aot [sistemas-l.core]
  :repl-optins {:init-ns sistemas-l.core})