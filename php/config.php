<?php

$manager = new MongoDB\Driver\Manager("mongodb://localhost:27017");
/* success, error messages to be displayed */
 $messages = array(
  1=>'Record deleted successfully',
  2=>'Error occurred. Please try again', 
  3=>'Record saved successfully',
  4=>'Record updated successfully', 
  5=>'All fields are required' );
  
?>