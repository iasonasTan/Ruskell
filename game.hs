import System.IO

drawP px py x y = do
  let c = (if px == x && py == y then 'X' else '*')
  putChar c

drawY px py x y w = do
  if x <= w then do
    --putStrLn "Drawing new x " ++ [x] ++ "..."
    drawP px py x y
    drawY px py (x+1) y w
  else 
    putStrLn ""

draw px py x y w h = do
  if y <= h then do
    --putStrLn "Drawing new line " ++ [y] ++ "..."
    drawY px py x y w
    draw px py x (y+1) w h
  else
    return ()

isWall x y w h = x == 0 || y == 0 || x == w || y == h

game opx opy w h = do
  d <- getChar
  if d == 'a' || d == 'd' || d == 'w' || d == 's' then do
    let npx = if d == 'a' && not (isWall (opx-1) opy w h) then opx - 1
              else if d == 'd' && not (isWall (opx+1) opy w h) then opx + 1
              else opx
    let npy = if d == 'w' && not (isWall opx (opy-1) w h) then opy - 1
              else if d == 's' && not (isWall opx (opy+1) w h) then opy + 1
              else opy
    putStr "\ESC[2J\ESC[H"
    draw npx npy 0 0 w h
    game npx npy w h
  else
    return ()

main = do
  hSetBuffering stdin NoBuffering
  hSetEcho stdout False
  game 2 2 10 10
