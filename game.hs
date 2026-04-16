import System.IO
import System.Random (randomRIO)

red    = "\ESC[31m"
green  = "\ESC[32m"
yellow = "\ESC[33m"
blue   = "\ESC[34m"
reset  = "\ESC[0m"

w :: Int
w = 20

h :: Int
h = 10

drawP px py tx ty x y = do
  let c = if px == x && py == y 
          then green ++ "X" ++ reset
          else if isWall x y 
          then red ++ "$" ++ reset
          else if tx == x && ty == y
          then yellow ++ "#" ++ reset
          else blue ++  "*" ++ reset
  putStr c

drawY px py tx ty x y = do
  if x <= w then do
    drawP px py tx ty x y
    drawY px py tx ty (x+1) y
  else 
    putStrLn ""

draw px py tx ty x y = do
  if y <= h then do
    drawY px py tx ty x y
    draw px py tx ty x (y+1)
  else
    return ()

isWall x y = x == 0 || y == 0 || x == w || y == h

target px py tx ty = tx == px && ty == py

moveTarget px py tx ty = do
  if (target px py tx ty)
  then do
    ntx <- randomRIO (1, w-1)
    nty <- randomRIO (1, h-1)
    return (ntx, nty) 
  else
    return (tx, ty)

-- Runs game loop infinetly if 'q' is not pressed.
game opx opy otx oty = do
  d <- getChar
  if d /= 'q' then do
    let npx = if d == 'a' && not (isWall (opx-1) opy) then opx - 1
              else if d == 'd' && not (isWall (opx+1) opy) then opx + 1
              else opx
    let npy = if d == 'w' && not (isWall opx (opy-1)) then opy - 1
              else if d == 's' && not (isWall opx (opy+1)) then opy + 1
              else opy
    (ntx, nty) <- moveTarget npx npy otx oty
    putStr "\ESC[2J\ESC[H"
    draw npx npy ntx nty 0 0
    game npx npy ntx nty
  else
    return ()

main = do
  hSetBuffering stdin NoBuffering
  hSetEcho stdout False
  putStrLn "Press any key to start."
  putStrLn "Move with WASD keys"
  game 2 2 1 1
