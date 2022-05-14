 input=$1
 echo "Compiling $input"
 java Compiler "$input"
 p=$(echo $input | cut -d\. -f1)
 c=$(echo ".java")
 f=$(echo "$p$c")
 javac "$f"
 echo "Running $p.class"
 java "$p"
