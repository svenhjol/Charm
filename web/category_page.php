<?php if (!defined('LAUNCHED')) die(); ?>

<div class="jumbotron jumbotron-fluid text-center charm-header">
    <div class="container">
        <h1 class="display-4"><span class="thin">Charm</span> <?php echo $category->getName(); ?></h1>
        <p class="lead"><?php echo $category->getDescription(); ?></p>
        <a class="btn btn-info" href="/"><span class="fa fa-home"></span> Back Home</a>
        <a class="btn btn-success" href="/download"><span class="fa fa-download"></span> Download Charm</a>
    </div>
</div>

<div class="container charm-category charm-category-<?php echo $category->getId(); ?>">

<?php

$features = Feature::getFeatures($category->getId());

foreach ($features as $id => $feature) {

?>

<div class="card charm-feature">
    <div class="row no-gutters">
        <div class="col-sm-3"><img src="images/features/card_<?php echo $feature->getId(); ?>.jpg" class="card-img" alt="Image for <?php echo $feature->getTitle(); ?>"></div>
        <div class="col-sm-9">
            <div class="card-body m-4">
                <h3 class="card-title"><?php echo $feature->getTitle(); ?></h5>
                <p class="card-text"><?php echo $feature->getTeaser(); ?></p>
                <?php if ($feature->hasBody()) { ?><button type="button" class="btn btn-info" data-toggle="modal" data-target="#<?php echo $feature->getId(); ?>"><span class="fa fa-book-open"></span>&nbsp;&nbsp;Read more</a><?php } ?>
            </div>
        </div>
    </div>
</div>

<?php if ($feature->hasBody()) { ?>
<div class="modal fade" id="<?php echo $feature->getId(); ?>" tabindex="-1" role="dialog" aria-labelledby="<?php echo $feature->getId(); ?>Label" aria-hidden="true">
<div class="modal-dialog modal-dialog-centered" role="document"><div class="modal-content">
    <div class="modal-header">
        <h3 class="modal-title" id="<?php echo $feature->getId(); ?>Label"><?php echo $feature->getTitle(); ?></h3>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><span class="fa fa-times"></span></button>
    </div>
    <div class="modal-body"><?php echo $feature->getBody(); ?></div>
</div></div>
</div>
<?php } ?>

<?php

}

?>

</div>