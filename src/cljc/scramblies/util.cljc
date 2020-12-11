(ns scramblies.util)

(defn- letters-counts [str]
  (reduce
    (fn [counts letter]
      (update
        counts
        letter
        (fn [value]
          (if (nil? value)
            1
            (inc value)))))
    {}
    str))

(defn scramble? [str1 str2]
  (if (and (string? str1) (string? str2) (not (empty? str2)))
    (let [letters-counts1 (letters-counts str1)
          letters-counts2 (letters-counts str2)]
      (loop [letters str2]
        (if (empty? letters)
          true
          (let [[letter & rest-letters] letters]
            (if (< (get letters-counts1 letter 0)
                   (get letters-counts2 letter))
              false
              (recur (rest rest-letters)))))))
    false))
