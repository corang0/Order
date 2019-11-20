<?php

	require_once('config.php');
	
	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
	
	if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$owner=$_POST['owner'];
		
		$filter = ['owner' => $owner];
		$options = [
			'sort' => ['_id' => -1],
		];
		$query = new MongoDB\Driver\Query($filter, $options);
		$cursor = $manager->executeQuery('sample.store', $query);
		
		$data = array();

		foreach ($cursor as $row) {
			$result = array();
			
			$result ['id'] = $row->id;
			$result ['name'] = $row->name;
			$result ['type'] = $row->type;
			$result ['address'] = $row->address;
			$result ['image'] = $row->image;
			
			array_push($data, $result);       
		}
		
		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("store"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
    }
	
?>