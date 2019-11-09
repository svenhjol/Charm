<?php if (!defined('LAUNCHED')) die(); ?>

<div class="jumbotron jumbotron-fluid text-center charm-header">
    <div class="container">
        <h1 class="display-4 big"><span class="thin">Charm</a></h1>
        <p class="lead">A vanilla+ mod for Minecraft 1.14</p>
        <a class="btn btn-info" href=""><span class="fa fa-question-circle"></span> Credits</a>
        <a class="btn btn-success" href="/download"><span class="fa fa-download"></span> Download Charm</a>
    </div>
</div>

<div class="container">

<?php

$categories = Category::getCategories();
foreach ($categories as $index => $category) {

?>

<div class="row">
    <div class="col-12">
        <div class="media text-center">
            <img src="" alt="" class="mr-3">
            <div class="media-body">
                <h3><a href="<?php echo $category->getId(); ?>"><?php echo $category->getName(); ?></a></h3>
                <p><?php echo $category->getDescription(); ?></p>
            </div>
        </div>
    </div>
</div>

<?php 

} 

?>

</div>