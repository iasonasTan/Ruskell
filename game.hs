import System.IO

red    = "\ESC[31m"
green  = "\ESC[32m"
yellow = "\ESC[33m"
blue   = "\ESC[34m"
reset  = "\ESC[0m"

w = 20
h = 10

drawP px py x y = do
  let c = if px == x && py == y 
          then green ++ "X" ++ reset
          else if isWall x y 
          then red ++ "$" ++ reset
          else blue ++  "*" ++ reset
  putStr c

drawY px py x y = do
  if x <= w then do
    drawP px py x y
    drawY px py (x+1) y
  else 
    putStrLn ""

draw px py x y = do
  if y <= h then do
    drawY px py x y
    draw px py x (y+1)
  else
    return ()

isWall x y = x == 0 || y == 0 || x == w || y == h

game opx opy = do
  d <- getChar
  if d /= 'q' then do
    let npx = if d == 'a' && not (isWall (opx-1) opy) then opx - 1
              else if d == 'd' && not (isWall (opx+1) opy) then opx + 1
              else opx
    let npy = if d == 'w' && not (isWall opx (opy-1)) then opy - 1
              else if d == 's' && not (isWall opx (opy+1)) then opy + 1
              else opy
    putStr "\ESC[2J\ESC[H"
    draw npx npy 0 0
    game npx npy
  else
    return ()

main = do
  hSetBuffering stdin NoBuffering
  hSetEcho stdout False
  putStrLn "Press any key to start."
  putStrLn "Move with WASD keys"
  game 2 2
