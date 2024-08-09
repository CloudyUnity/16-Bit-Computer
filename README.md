This is a 16-bit computer built from basic logic components.  
This program is split into 5 parts. Only Logic Builder can be run yourself without some significant effort.  
Classes that start with an underscore belong to the compiler-translator-assembler-computer parts.  
This program is inspired and based on the Nand2Tetris course, especially the later sections  

## Logic Builder  

This is the program which runs by default when built  
This program is inspired by Logism and Sebastian Lagues computer simulator  
You can place primitive chips such as AND and NOT to create a new input-output circuit  
You can then save this circuit as a new chip to be placed  

### How to use

The circles labelled a, b, c, ... on the left are inputs  
The circles labelled 0, 1, 2, ... on the right are outputs  
Use the + and - buttons to change the input/output counts  
Click and drag the AND/NOT buttons to create the respective gates  
Drag circles to others to make a connection  
Type a name into the top input field and press SAVE to save the chip  
View Data/Logic Builder/Blocks to see the chips you've created  
Type the name of a created chip then click and drag Get to make an instance of the chip  
Hold the middle mouse button to pan the camera around  
Drag a chip onto Delete to delete it  
Press Restart to remove all chips currently placed  
Press the P key to disable updating, this can improve performance while placing chips in extremely complex chips such as RAM  

## Java Computer

This is a java recreation of a 16-bit computer which can run Hack machine code  
It has 40k 16-bit registers, or 80KB  
It has a seperate ROM which can be any length. (Although label addresses can only be so high)  

## Hack Assembler

This program converts Hack assembly to machine code  
Hack assembly is a RISC architechture assembly with only 2 registers  

## Jack VM Translator

This program converts Jack VM to Hack assembly  
Jack VM is an intermediary language with a focus on multiple stacks for storing temporary data  

## Jack Compiler

This program compiles Jack to Jack VM   
This works in two passes, first it converts to XML then XML to Jack VM  
There's a lot of cool stuff happening here but it's been a while between me writing it and me making this README so I can't remember all of it  

![image](https://github.com/user-attachments/assets/6504503c-1444-4557-ada4-30e20747b4e3)

