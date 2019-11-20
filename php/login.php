<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

	require_once('config.php');
	
	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
	
	if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$id=$_POST['id'];
		$password=$_POST['password'];
		
		$filter = ['id' => $id, 'password' => $password];
		$options = [
			'sort' => ['_id' => -1],
		];
		$query = new MongoDB\Driver\Query($filter, $options);
		$cursor = $manager->executeQuery('sample.user', $query);
		
		$data = array();

		foreach ($cursor as $row) {
			echo $row->id;
			
		}
    }
	
?>