# marketplace

Library to be used by the UI team that provides live order board
functionality.  Use by creating an instance of the LiveOrderBoardService
which provides methods for registering and cancelling orders along with
the getLiveOrderBoard method that returns a LiveOrderBoard.  

The LiveOrderBoard has a list of the buy and sell orders that are sorted
in display order ready to be rendered in the UI. 
  