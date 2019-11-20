<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

	require_once('config.php');
	
	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
	
	if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$id=$_POST['id'];
		
		$filter = ['id' => $id];
		$options = [
			'sort' => ['_id' => -1],
		];
		$query = new MongoDB\Driver\Query($filter, $options);
		$cursor = $manager->executeQuery('sample.store', $query);

		$data = array();

		foreach ($cursor as $row) {
			$data = $row->product;    
		}

		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("product"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
    }
	
?>