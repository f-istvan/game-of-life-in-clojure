(ns game-of-life.run
  (:require [clojure.java.io :as io]))

;; rules:
;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
;; 2) Any live cell with two or three live neighbours lives on to the next generation.
;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.

(defn print-map
  [game]
  (println game))

(defrecord Game
    [iteration-delay-in-millis
     map-size
     living-cells]

  Object
  (toString [_]
    (str "Iteration delay in millis: " iteration-delay-in-millis
         " Map size: " map-size
         " Living cells: " living-cells)))

(defn get-all-surrounding-cells
  [cell]
  ;;(println (str "here cell " cell))
  (let [cell-x (get cell 0)
        cell-y (get cell 1)]
    (for [x (range (dec cell-x) (+ cell-x 2))
          y (range (dec cell-y) (+ cell-y 2))]
      [x y])))

(defn count-duplicates
  [a b]
  (let [concatd (concat a b)]
    (- (count concatd) (count (set concatd)))))

(defn gonna-live?
  [cell living-cells]
  (let [all-surrounding-cells (get-all-surrounding-cells cell)
        living-neighbours (dec (count-duplicates all-surrounding-cells living-cells))
        is-a-living-cell (count-duplicates living-cells [cell])]

   ; (println (str living-neighbours "-" is-a-living-cell "-" living-cells "-" cell))

    (if is-a-living-cell
      (cond
        ;; 1) Any live cell with fewer than two live neighbours dies, as if caused by under-population.
        (< living-neighbours 2) false

        ;; 2) Any live cell with two or three live neighbours lives on to the next generation.
        (or (== living-neighbours 2) (== living-neighbours 3)) true

        ;; 3) Any live cell with more than three live neighbours dies, as if by overcrowding.
        (> living-neighbours 3) false
        )

      ;; 4) Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
      (if (== living-neighbours 3) true false)
  )))

(defn get-all-cells
  [map-size]
  ;;(println "get all cells")
  (for [x (range (:x map-size))
        y (range (:y map-size))]
    [x y]))

(defn create-next-generation
  [game]
  (let [next-generation
        (for [cell (get-all-cells (:map-size game))
              :when (gonna-live? cell (:living-cells game))]
          cell)]

    (doall next-generation)
    (print-map game)
    ;;(println next-generation)
    (Game.
          (:iteration-delay-in-millis game)
          (:map-size game)
          next-generation)))

(defn run-game
  [game]
  (let [delay (:iteration-delay-in-millis game)]
    (loop [game game]
        ;;(print-map game)
        (Thread/sleep delay)
        (recur (create-next-generation game)))))

(defn read-game-data-from-file
  [filename]
  (let [data-file (io/resource filename)
        init-state (read-string (slurp data-file))
        game (Game.
              (:iteration-delay-in-millis init-state)
              (:map-size init-state)
              (:living-cells init-state))]
    game))

(defn -main
  [& args]
  (let [game (read-game-data-from-file "init-world.txt")]
    (println "Starting Game Of Life...")
    (run-game game)))
