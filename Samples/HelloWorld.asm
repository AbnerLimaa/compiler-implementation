    .globl main
	.text
main:
L7:
	li $v0, 4
	mulo $t8, $v0, 1
	move $a0, $t8
	move $a1, $0
	jal _initRecord
	move $t4, $v0
	move $v0, $t4
	li $a1, 10
	move $a0, $a1
	jal PrintPrintNumber
	jal _printint
	b L6
L6:
#	return
    li $v0, 10       
    syscall          

	.text
PrintPrintNumber:
PrintPrintNumber_framesize=4
	subu $sp, $sp, PrintPrintNumber_framesize
L9:
	move $fp, $ra
	move $k0, $t5
	move $v1, $s1
	move $k1, $s2
	move $s2, $s3
	move $0, $s4
	move $s5, $s5
	move $gp, $s6
	move $s7, $s7
	move $s6, $t0
	move $s3, $v0
	move $s4, $a0
L2:
	blt $s4, 21, L0
L1:
	move $t0, $s6
	move $s7, $s7
	move $s6, $gp
	move $s5, $s5
	move $s4, $0
	move $s3, $s2
	move $s2, $k1
	move $s1, $v1
	move $t5, $k0
	move $ra, $fp
	b L8
L0:
	move $a0, $s4
	jal _printint
	add $s1, $s4, 1
	move $s4, $s1
	b L2
L8:
#	return
	addu $sp, $sp, PrintPrintNumber_framesize
	j $ra
	.text
PrintPrintNumb:
PrintPrintNumb_framesize=4
	subu $sp, $sp, PrintPrintNumb_framesize
L11:
	move $gp, $ra
	move $0, $s0
	move $v1, $s1
	move $s2, $s2
	move $s0, $s3
	move $fp, $s4
	move $s3, $s5
	move $k1, $s6
	move $s6, $s7
	move $s1, $a2
	move $t2, $v0
	move $s7, $a0
	li $s5, 100
	move $a0, $s5
	jal _printint
	move $v0, $s7
	move $a2, $s1
	move $s7, $s6
	move $s6, $k1
	move $s5, $s3
	move $s4, $fp
	move $s3, $s0
	move $s2, $s2
	move $s1, $v1
	move $s0, $0
	move $ra, $gp
	b L10
L10:
#	return
	addu $sp, $sp, PrintPrintNumb_framesize
	j $ra
	.text
PrintPrintFactorial:
PrintPrintFactorial_framesize=4
	subu $sp, $sp, PrintPrintFactorial_framesize
L13:
	move $t3, $ra
	move $s1, $s0
	move $fp, $a1
	move $gp, $s2
	move $0, $s3
	move $t6, $s4
	move $s5, $s5
	move $s6, $s6
	move $t9, $s7
	move $s2, $a2
	move $s3, $v0
	move $k1, $a0
	blt $k1, 1, L3
L4:
	li $k0, 300
	move $a0, $k0
	jal _printint
	sub $t0, $k1, 1
	move $s4, $t0
	li $ra, 10
	move $t1, $ra
L5:
	move $v0, $t1
	move $a2, $s2
	move $s7, $t9
	move $s6, $s6
	move $s5, $s5
	move $s4, $t6
	move $s3, $0
	move $s2, $gp
	move $a1, $fp
	move $s0, $s1
	move $ra, $t3
	b L12
L3:
	li $t2, 500
	move $a0, $t2
	jal _printint
	li $t8, 1
	move $t1, $t8
	b L5
L12:
#	return
	addu $sp, $sp, PrintPrintFactorial_framesize
	j $ra
   .text             
_initArray:          
   sll $a0,$a0,2     
   li $v0,9          
   syscall           
   move $a2,$v0      
   b Lrunt2          
   Lrunt1:           
   sw $a1,($a2)      
   sub $a0,$a0,4     
   add $a2,$a2,4     
   Lrunt2:           
   bgtz $a0, Lrunt1  
   j $ra             
                     
_initRecord:         
   li $v0,9          
   syscall           
   move $a2,$v0      
   b Lrunt4          
   Lrunt3:           
   sw $0,($a2)       
   sub $a0,$a0,4     
   add $a2,$a2,4     
   Lrunt4:           
   bgtz $a0, Lrunt3  
   j $ra             
                     
_stringEqual:        
   beq $a0,$a1,Lrunt10
   lw  $a2,($a0)     
   lw  $a3,($a1)     
   addiu $a0,$a0,4   
   addiu $a1,$a1,4   
   beq $a2,$a3,Lrunt11
   Lrunt13:          
   li  $v0,0         
   j $ra             
   Lrunt12:          
   lbu  $t0,($a0)    
   lbu  $t1,($a1)    
   bne  $t0,$t1,Lrunt13
   addiu $a0,$a0,1   
   addiu $a1,$a1,1   
   addiu $a2,$a2,-1  
   Lrunt11:          
   bgez $a2,Lrunt12  
   Lrunt10:          
   li $v0,1          
   j $ra             
                     
_print:              
   lw $a1,0($a0)     
   add $a0,$a0,4     
   add $a2,$a0,$a1   
   lb $a3,($a2)      
   sb $0,($a2)       
   li $v0,4          
   syscall           
   sb $a3,($a2)      
   j $ra             
                     
_flush:              
	j $ra             
                     
.data                
Runtconsts:          
.space 2048          
Runtempty: .word 0   
                     
.text                
                     
_main:               
   li $a0,0          
   la $a1,Runtconsts 
   li $a2,1          
   Lrunt20:          
   sw $a2,($a1)      
   sb $a0,4($a1)     
   addiu $a1,$a1,8   
   slt $a3,$a0,256   
   bnez $a3,Lrunt20  
   li $a0,0          
   j t_main          
                     
_ord:                
   lw $a1,($a0)      
   li $v0,-1         
   beqz $a1,Lrunt5   
   lbu $v0,4($a0)    
   Lrunt5:           
	j $ra             
                     
.data                
Lrunt30: .asciiz "character out of range\n"
.text                
                     
chr:                 
   andi $a1,$a0,255  
   bnez $a1,Lrunt31  
   sll  $a0,$a0,3    
   la   $v0,Runtconsts($a0)
   j $ra             
   Lrunt31:          
   li   $v0,4        
   la   $a0,Lrunt30  
   syscall           
   li   $v0,10       
   syscall           
                     
_size:               
   lw $v0,($a0)      
	j $ra             
                     
.data                
Lrunt40:  .asciiz "substring out of bounds\n"
                     
_substring:          
   lw $t1,($a0)      
   bltz $a1,Lrunt41  
   add $t2,$a1,$a2   
   sgt $t3,$t2,$t1   
   bnez $t3,Lrunt41  
   add $t1,$a0,$a1   
   bne $a2,1,Lrunt42 
   lbu $a0,($t1)     
   b chr             
   Lrunt42:          
   bnez $a2,Lrunt43  
   la  $v0,Lruntempty
   j $ra             
   Lrunt43:          
   addi $a0,$a2,4    
   li   $v0,9        
   syscall           
   move $t2,$v0      
   Lrunt44:          
   lbu  $t3,($t1)    
   sb   $t3,($t2)    
   addiu $t1,1       
   addiu $t2,1       
   addiu $a2,-1      
   bgtz $a2,Lrunt44  
   j $ra             
   Lrunt41:          
   li   $v0,4        
   la   $a0,Lrunt40  
   syscall           
   li   $v0,10       
	syscall           
                     
_concat:             
   lw $t0,($a0)      
   lw $t1,($a1)      
   beqz $t0,Lrunt50  
   beqz $t1,Lrunt51  
   addiu  $t2,$a0,4  
   addiu  $t3,$a1,4  
   add  $t4,$t0,$t1  
   addiu $a0,$t4,4   
   li $v0,9          
   syscall           
   addiu $t5,$v0,4   
   sw $t4,($v0)      
   Lrunt52:          
   lbu $a0,($t2)     
   sb  $a0,($t5)     
   addiu $t2,1       
   addiu $t5,1       
   addiu $t0,-1      
   bgtz $t0,Lrunt52  
   Lrunt53:          
   lbu $a0,($t4)     
   sb  $a0,($t5)     
   addiu $t4,1       
   addiu $t5,1       
   addiu $t2,-1      
   bgtz $t2,Lrunt52  
   j $ra             
   Lrunt50:          
   move $v0,$a1      
   j $ra             
   Lrunt51:          
   move $v0,$a0      
   j $ra             
                     
_not:                
   seq $v0,$a0,0     
	j $ra             
                     
.data                
getchbuf: .space 200    
getchptr: .word getchbuf
                     
.text                
_getchar:            
    lw  $a0,getchptr 
    lbu $v0,($a0)    
    add $a0,$a0,1    
    bnez $v0,Lrunt6  
    li $v0,8         
    la $a0,getchbuf  
    li $a1,200       
    syscall          
    la $a0,getchbuf  
    lbu $v0,($a0)    
    bnez $v0,Lrunt6  
    li $v0,-1        
    Lrunt6:          
    sw $a0,getchptr  
    j $ra            
    .text            
    .globl _halloc   
_halloc:             
    li $v0, 9        
    syscall          
    j $ra            
                     
    .text            
    .globl _printint 
_printint:           
    li $v0, 1        
    syscall          
    la $a0, newl     
    li $v0, 4        
    syscall          
    j $ra            
                     
    .data            
    .align   0       
newl:    .asciiz "\n"  
    .data           
    .align   0      
str_er:  .asciiz " ERROR: abnormal termination\n"                     
   .text           
   .globl _error    
_error:             
   li $v0, 4        
   la $a0, str_er   
   syscall          
   li $v0, 10       
	syscall          

