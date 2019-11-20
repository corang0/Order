<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    require_once('config.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$name=$_POST['name'];
		$type=$_POST['type'];
        $address=$_POST['address'];
		$owner = $_POST['owner'];
		$image = $_POST['image'];

        if(empty($name)){
            $errMSG = "이름을 입력하세요.";
        }
        else if(empty($type)){
            $errMSG = "분류를 입력하세요.";
        }
		else if(empty($address)){
			$errMSG = "주소를 입력하세요.";
		}
		
		if(!isset($errMSG)){
			$filter = [];
			$options = [
				'sort' => ['_id' => -1],
			];
			$query = new MongoDB\Driver\Query($filter, $options);
			$id = (string)(count($manager->executeQuery('sample.store', $query)->toArray()) + 1);
			
			$insRec       = new MongoDB\Driver\BulkWrite;
			$insRec->insert(['id'=>$id, 'name'=>$name, 'type'=>$type, 'address'=>$address, 'image'=>$image, 'product'=>[], 'owner'=>$owner]);

            $writeConcern = new MongoDB\Driver\WriteConcern(MongoDB\Driver\WriteConcern::MAJORITY, 1000);
         
            $result       = $manager->executeBulkWrite('sample.store', $insRec, $writeConcern);
			if($result->getInsertedCount()){
				$successMSG = "새로운 데이터를 추가했습니다.";
			}
			else{
				$errMSG = "데이터 추가 에러";
			}
        }
    }
	
?>

<?php 
    if (isset($errMSG)) echo $errMSG;
    if (isset($successMSG)) echo $successMSG;

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");
   
    if( !$android )
    {
?>
    <html>
       <body>

            <form action="<?php $_PHP_SELF ?>" method="POST">
                Name: <input type = "text" name = "name" />
                Type: <input type = "text" name = "type" />
				Address: <input type = "text" name = "address" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>