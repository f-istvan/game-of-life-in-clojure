(ns game-of-life.run-test
  (:require [clojure.test :refer :all]
            [game-of-life.run :refer :all]))

(deftest get-all-surrounding-cells-test
  (testing "negative values in the left column"
    (is (= (get-all-surrounding-cells [0 10]) [[-1 9] [-1 10] [-1 11]
                                               [0 9] [0 10] [0 11]
                                               [1 9] [1 10] [1 11]])))

  (testing "negative values in the bottom row"
    (is (= (get-all-surrounding-cells [10 0]) [[9 -1] [9 0] [9 1]
                                               [10 -1] [10 0] [10 1]
                                               [11 -1] [11 0] [11 1]])))

  (testing "zero values in the right column"
    (is (= (get-all-surrounding-cells [-1 5]) [[-2 4] [-2 5] [-2 6]
                                               [-1 4] [-1 5] [-1 6]
                                               [0 4] [0 5] [0 6]])))

  (testing "zero values in the top row"
    (is (= (get-all-surrounding-cells [13 -1]) [[12 -2] [12 -1] [12 0]
                                                [13 -2] [13 -1] [13 0]
                                                [14 -2] [14 -1] [14 0]]))))

(deftest count-duplicates-test
  (testing "empty imput have zero duplicates"
    (is (= (count-duplicates [] []) 0)))

  (testing "returns zero when there is no duplicate"
    (is (= (count-duplicates [8] [9]) 0))
    (is (= (count-duplicates [8 4] [9 6 7 2]) 0))
    (is (= (count-duplicates ["a"] ["d" "f" "g" "n"]) 0))
    (is (= (count-duplicates [[0 0] [3 3] [2 2]] [[4 4] [7 7] [-1 -1]]) 0))
    (is (= (count-duplicates [[0 1]] [[1 0]]) 0))
    (is (= (count-duplicates [[0 0] [0 1] [1 0] [1 1]] [[9 9]]) 0)))

  (testing "returns one when there is only one duplicate"
    (is (= (count-duplicates [7] [7]) 1))
    (is (= (count-duplicates [[1 0]] [[1 0]]) 1)))

  (testing "it is reflective"
    (is (= (count-duplicates [1 2 3 4 5 6 7] [55 54 65 7 444 2 11 4 22 33]) 3))
    (is (= (count-duplicates [55 54 65 7 444 2 11 4 22 33] [1 2 3 4 5 6 7]) 3))
    (is (= (count-duplicates [[1 0] [8 3] [45 7]] [[1 0] [4 5] [7 7] [8 3] [12 4] [45 7]]) 3))
    (is (= (count-duplicates [[1 0] [4 5] [7 7] [8 3] [12 4] [45 7]] [[1 0] [8 3] [45 7]]) 3)))

  (testing "returns one when there is only one duplicate in the middle"
    (is (= (count-duplicates [1] [0 1 4]) 1))
    (is (= (count-duplicates [[3 5]] [[5 3] [3 5] [1 1]]) 1))))

(deftest gonna-live?-test
  (testing "Any live cell with fewer than two live neighbours dies"
    (is (false? (gonna-live? [5 5] [[5 5] [20 20]])))
    (is (false? (gonna-live? [5 5] [[5 5] [7 7] [20 20]])))
    (is (false? (gonna-live? [5 5] [[5 5] [7 7] [5 6] [20 20]]))))

  (testing "Any live cell with two or three live neighbours lives"
    (is (true? (gonna-live? [5 5] [[5 5] [5 4] [5 6] [20 20]])))
    (is (true? (gonna-live? [5 5] [[5 5] [4 5] [4 5] [20 20]])))
    (is (true? (gonna-live? [5 5] [[5 5] [6 6] [4 6] [20 20]])))
    (is (true? (gonna-live? [5 5] [[5 5] [6 6] [4 6] [6 4] [20 20]])))
    (is (true? (gonna-live? [5 5] [[5 5] [5 4] [5 6] [4 5] [20 20]])))))

(deftest gonna-live?-test
  (testing "Any live cell with more than three live neighbours dies"
    (is (false? (gonna-live? [5 5] [[5 5] [5 6] [5 4] [4 5] [6 5] [20 20]])))
    (is (false? (gonna-live? [5 5] [[5 5] [6 4] [6 6] [4 4] [4 6] [20 20]])))))

(deftest gonna-live?-test
  (testing "Any dead cell with exactly three live neighbours becomes a live cell"
    (is (true? (gonna-live? [5 5] [[5 6] [5 4] [4 5] [20 20]])))
    (is (true? (gonna-live? [5 5] [[6 4] [6 6] [4 4] [20 20]])))))
