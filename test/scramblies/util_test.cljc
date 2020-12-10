(ns scramblies.util-test
  (:require [clojure.test :refer :all])
  (:require [scramblies.util :refer [scramble?]]))

(deftest scramble-test
  (is (= (scramble? "rekqodlw" "world") true))
  (is (= (scramble? "cedewaraaossoqqyt" "codewars") true))
  (is (= (scramble? "katas" "steak") false)))
