(defproject depict "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.mikera/imagez "0.12.0"]
                 [org.openscience.cdk/cdk-core "1.5.14"]
                 [org.openscience.cdk/cdk-data "1.5.14"]
                 [org.openscience.cdk/cdk-depict "1.5.14"]
                 [org.openscience.cdk/cdk-io "1.5.14"]
                 [org.openscience.cdk/cdk-smiles "1.5.14"]
                 [gif-clj "1.0.3"]]



  :main ^:skip-aot depict.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
