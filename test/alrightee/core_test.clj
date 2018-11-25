(ns alrightee.core-test
  (:require [alrightee.core :as tee]
            [clojure.test :refer [deftest testing is]]))

(defn- my-function [m x]
  (assoc m ::my-function x))

(deftest tee-function-test
  (let [sink-call (atom nil)]
    (testing "can tee"
      (tee/tee #'my-function
               [(fn sink [m x]
                  (reset! sink-call [m x]))])

      (is (= {:foo "bar"
              ::my-function "baz"}
             (my-function {:foo "bar"} "baz")))

      (is (= [{:foo "bar"} "baz"]
             @sink-call)))

    (testing "and untee"
      (tee/un-tee #'my-function)

      (is (= {:quu "quux"
              ::my-function "baz"}
             (my-function {:quu "quux"} "baz")))

      ;; unchanged
      (is (= [{:foo "bar"} "baz"]
             @sink-call)))))
