<?php

	error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    require_once('config.php');

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if( (($_SERVER['REQUEST_METHOD'] == 'POST') && isset($_POST['submit'])) || $android )
    {   
		$id=$_POST['id'];
		$name=$_POST['name'];
		$type=$_POST['type'];
        $price=$_POST['price'];

        if(empty($name)){
            $errMSG = "이름을 입력하세요.";
        }
        else if(empty($type)){
            $errMSG = "분류를 입력하세요.";
        }
		else if(empty($price)){
			$errMSG = "가격을 입력하세요.";
		}
		
		if(!isset($errMSG)){				
			$insRec       = new MongoDB\Driver\BulkWrite;
            $insRec->update(['id' => $id],['$push' =>['product' =>['name'=>$name, 'type'=>$type, 'price'=>$price, 'image'=>"0"]]], ['multi' => false, 'upsert' => false]);

            $writeConcern = new MongoDB\Driver\WriteConcern(MongoDB\Driver\WriteConcern::MAJORITY, 1000);
         
            $result       = $manager->executeBulkWrite('sample.store', $insRec, $writeConcern);
			if($result->getModifiedCount()){
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
				Address: <input type = "text" name = "price" />
                <input type = "submit" name = "submit" />
            </form>
       
       </body>
    </html>

<?php 
    }
?>