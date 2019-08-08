Worked with: ban20

# Part I

> CellSociety is a class that handles backend, the only public interface it has
  is the ability to get a specific generation, given a generation number

> CellSociety handles updates and interactions with main, Cell
  triggers actions, Grid keeps track of all the cells

> I am trying to leave as little loose ends as possible,
  only essential parts are public, the rest is private, package-private or protected
 
> I throw exceptions when configuration is invalid. Idea being,
  make configuration comprehensive enough, so that is it is valid,
  there can never be an error withing the code

> This design helps create a versatile and scalable structure
  to be able to implement almost any 2D Cell Society imaginable

# Part 2

> As little dependencies as possible, trying to keep the parts of the code separate
and only interact on user input with the UI

> They depend on helper-classes, such as Cell and Grid

> No need for Interface or an abstraction for a static method

# Part 3

- Use cases
  - Initialize cell grid
  - Update cells
  - Settle conflicts
  - Assign actions
  - Change the size of the grid
 
- Excited to work on actions
- Worried about conflicts during those actions
  