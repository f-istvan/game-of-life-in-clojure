(ns game-of-life.run-test
  (:require [clojure.test :refer :all]
            [game-of-life.run :refer :all]))

(deftest a-test
  (testing "Testing test-nf..."
    (is (= (test-fn 42) 42))))
