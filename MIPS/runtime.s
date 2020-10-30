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