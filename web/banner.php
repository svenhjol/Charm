<?php if (!defined('LAUNCHED')) die(); ?>

<div class="jumbotron jumbotron-fluid text-center charm-banner <?php if (is_null($category)) echo 'charm-banner-home'; ?>">
    <div class="container">
        <h1 class="display-4 big"><?php echo $bannerTitle; ?></h1>
        <p class="lead"><?php echo $bannerSubtitle; ?></p>
        <?php if (is_null($category)) { ?>
        <a class="btn btn-info" href="/"><span class="fa fa-question-circle"></span> Credits</a>
        <?php } else { ?>    
        <a class="btn btn-info" href="/"><span class="fa fa-home"></span> Home</a>
        <?php } ?>
        <a class="btn btn-success" href="https://www.curseforge.com/minecraft/mc-mods/charm/files/2831913"><span class="fa fa-download"></span> Download Charm</a>
    </div>
</div>
